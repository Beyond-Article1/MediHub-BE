package mediHub_be.board.Util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

// 조회수 증가 공통 로직
@Component
public class ViewCountManager {

    private static final String COOKIE_NAME ="viewPosts";


    public boolean shouldIncreaseViewCount(Long postId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                // 이미 조회한 게시글인 경우 (쿠키에 게시글 id가 포함된 경우)
                if(COOKIE_NAME.equals(cookie.getName())&& cookie.getValue().contains("[" + postId + "]")) {
                    return false;
                }
            }
        }

        //그 외 (조회하지 않은 게시글인 경우)
        String newCookieValue = "["+postId+"]";

        if(cookies!=null) {
            for(Cookie cookie : cookies) {
                //이미 쿠키가 존재하면 기존 값에 추가.
                if(COOKIE_NAME.equals(cookie.getName())){
                    newCookieValue = cookie.getValue()+"[" + postId + "]";
                }
            }
        }

        Cookie newCookie = new Cookie(COOKIE_NAME, newCookieValue);
        newCookie.setPath("/");
        newCookie.setMaxAge(24*60*60); //24시간
        response.addCookie(newCookie); //클라이언트로 쿠키 전송

        return true;

    }

}
