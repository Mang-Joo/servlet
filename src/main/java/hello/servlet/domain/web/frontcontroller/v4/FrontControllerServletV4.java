package hello.servlet.domain.web.frontcontroller.v4;

import hello.servlet.domain.web.frontcontroller.MyView;
import hello.servlet.domain.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.domain.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.domain.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private final Map<String, ControllerV4> controllerV4Map = new HashMap<>();

    public FrontControllerServletV4() {
        controllerV4Map.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerV4Map.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerV4Map.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerV4Map.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //paramMap을 myView에 넘거주어야한다.
        Map<String, String> paramMap = createParamMap(request); // request.getParameter를 사용하는 부분
        Map<String, Object> model = new HashMap<>(); // 추가된 부분

        String viewName = controller.process(paramMap, model);
        MyView myView = viewResolver(viewName);
        myView.render(model, request, response);


    }

    // view의 이름만 반환한것을 prefix와 suffix를 처리한다.
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    //파라미터를 다 가져와서 Map으로 반환한다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
