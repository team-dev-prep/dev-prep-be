package com.example.Backend.auth;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.user.User;
import com.example.Backend.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/github/callback")
    public ResponseEntity<?> githubCallbackViaPost(@RequestBody Map<String, String> payload,
                                                   HttpServletResponse response) {
        try {
            String code = payload.get("code");

            if (code == null || code.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Authorization code is missing"));
            }

            String accessToken = authService.getAccessToken(code);
            User user = authService.getUserInfoAndSave(accessToken);

            String jwtAccess = jwtUtil.generateAccessToken(user.getId().toString());
            String jwtRefresh = jwtUtil.generateRefreshToken(user.getId().toString());

            // Access Token 쿠키 설정
            ResponseCookie accessCookie = ResponseCookie.from("access_token", jwtAccess)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(60 * 60 * 3)  // 3시간
                    .domain("devprep-official.store")
                    .build();

            // Refresh Token 쿠키 설정
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", jwtRefresh)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(60 * 60 * 24 * 7)  // 7일
                    .domain("devprep-official.store")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            return ResponseEntity.ok(Map.of("message", "Login success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "GitHub callback 처리 중 오류 발생"));
        }
    }

    @Operation(summary = "로그인된 유저 정보 조회", description = "access_token 쿠키가 유효하면 로그인한 유저의 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 없음 또는 유효하지 않음")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getLoginUser(@CookieValue(name = "access_token", required = false) String token) {
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token is missing");

        try {
            String userId = jwtUtil.validateAndExtractSubject(token);
            return userRepository.findById(Long.valueOf(userId))
                    .map(user -> ResponseEntity.ok(Map.of(
                            "id", user.getId(),
                            "name", user.getName(),
                            "avatar", user.getAvatarUrl()
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Access token is missing")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Logout", description = "access_token 및 refresh_token 쿠키를 지웁니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie expiredAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)  // 만료시간을 0으로 설정하여 삭제
                .domain("devprep-official.store")
                .build();

        ResponseCookie expiredRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)  // 만료시간을 0으로 설정하여 삭제
                .domain("devprep-official.store")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefresh.toString());

        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }

    @Operation(summary = "자동 로그인용 Access Token 재발급", description = "Refresh Token만으로 Access Token을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access token reissued"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }

        try {
            String userId = jwtUtil.validateAndExtractSubject(refreshToken);

            return userRepository.findById(Long.valueOf(userId))
                    .map(user -> {
                        String newAccessToken = jwtUtil.generateAccessToken(userId);
                        ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                                .httpOnly(true)
                                .secure(true)
                                .sameSite("None")
                                .path("/")
                                .maxAge(60 * 180)  // 3시간
                                .domain("devprep-official.store")
                                .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                        return ResponseEntity.ok(Map.of("message", "Access token refreshed"));
                    })
                    .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "User not found")));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
