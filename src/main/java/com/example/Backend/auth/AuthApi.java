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
    @Operation(summary = "GitHub OAuth Callback", description = "GitHub OAuth 흐름에서 콜백을 처리하고 JWT 토큰을 생성하고 쿠키를 설정합니다.")
    @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Login successful"),
                @ApiResponse(responseCode = "401", description = "Authentication failed")
            })
    @GetMapping("/github/callback")
    public ResponseEntity<?> githubCallback(@RequestParam String code, HttpServletResponse response) {
        String accessToken = authService.getAccessToken(code);
        User user = authService.getUserInfoAndSave(accessToken);

        String jwtAccess = jwtUtil.generateAccessToken(user.getId().toString());
        String jwtRefresh = jwtUtil.generateRefreshToken(user.getId().toString());

        ResponseCookie accessCookie = ResponseCookie.from("access_token", jwtAccess)
                .httpOnly(true).secure(false).sameSite("None").path("/").maxAge(1800).build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", jwtRefresh)
                .httpOnly(true).secure(false).sameSite("None").path("/").maxAge(604800).build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Login success"));
    }

    @Operation(summary = "GitHub Login", description = "GitHub OAuth 코드를 처리하여 사용자를 인증하고 JWT 토큰을 생성하며 쿠키를 설정합니다.")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Login successful"),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "401", description = "Authentication failed")
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        String code = payload.get("code");

        String githubAccessToken = authService.getAccessToken(code);
        User user = authService.getUserInfoAndSave(githubAccessToken);

        String jwtAccess = jwtUtil.generateAccessToken(user.getId().toString());
        String jwtRefresh = jwtUtil.generateRefreshToken(user.getId().toString());

        ResponseCookie accessCookie = ResponseCookie.from("access_token", jwtAccess)
                .httpOnly(true).secure(true).path("/").maxAge(1800).build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", jwtRefresh)
                .httpOnly(true).secure(true).path("/").maxAge(604800).build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Login success"));
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
                            "email", user.getEmail(),
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
                .httpOnly(true).secure(true).path("/").maxAge(0).build();
        ResponseCookie expiredRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefresh.toString());

        return ResponseEntity.ok(Map.of("message", "Logout success"));
    }

    @Operation(summary = "Reissue Access Token", description = "refresh_token 을 사용하여 access_token 을 다시 발급합니다.")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Access token reissued successfully"),
                @ApiResponse(responseCode = "401", description = "Refresh token is missing or invalid")
            })
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }

        try {
            String userId = jwtUtil.validateAndExtractSubject(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(userId);

            ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                    .httpOnly(true).secure(true).path("/").maxAge(1800).build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            return ResponseEntity.ok(Map.of("message", "Access token reissued"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
