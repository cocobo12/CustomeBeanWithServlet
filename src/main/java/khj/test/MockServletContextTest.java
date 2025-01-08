package khj.test;

import jakarta.servlet.*;
import khj.container.MockServletContext;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// 서블릿 테스트
public class MockServletContextTest {

    @Test
    void testAddServlet() throws ServletException, IOException {
        // MockServletContext 인스턴스 생성
        MockServletContext mockContext = new MockServletContext();

        // 서블릿 인스턴스 생성 (간단한 서블릿 예시)
        Servlet myServlet = new MyServlet();

        // 서블릿을 MockServletContext에 등록
        MockServletContext.MockServletRegistration registration =
                (MockServletContext.MockServletRegistration) mockContext.addServlet("MyServlet", myServlet);

        // 서블릿이 정상적으로 등록되었는지 확인
        assertNotNull(registration);

        // URL 패턴 매핑 추가
        registration.addMapping("/test");

        // 등록된 URL 패턴이 올바르게 매핑되었는지 확인
        assertTrue(registration.getMappings().contains("/test"));

        // 서블릿 인스턴스를 직접 사용하여 service() 메서드 실행
        // 등록된 서블릿을 가져와서 서비스 실행
        myServlet.service(new MockServletRequest(), new MockServletResponse());
    }

    // 서블릿 클래스 예시
    static class MyServlet implements Servlet {
        @Override
        public void destroy() {
            // 종료 처리 로직
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
            // 초기화 로직
        }

        @Override
        public ServletConfig getServletConfig() {
            return null;
        }

        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            // JSP 파일 경로
            String jspFilePath = "/WEB-INF/views/home-form.jsp";

            // JSP 파일을 로드하고 내용을 콘솔에 출력
            String content = loadJspContent(jspFilePath);

            // 로드된 JSP 파일 내용 출력
            System.out.println("JSP content:\n" + content);

            // 응답 객체에 내용을 작성 (필요시)
            res.getWriter().write("JSP content: " + content);
        }

        @Override
        public String getServletInfo() {
            return "MyServlet Info";
        }

        // JSP 파일을 로드하는 메서드
        private String loadJspContent(String jspFilePath) throws IOException {
            // 파일 경로를 실제 경로로 매핑 (테스트 환경에서는 가상의 경로 사용)
            // 예: "src/main/webapp/WEB-INF/views/home-form.jsp"
            String fullPath = "src/main/webapp" + jspFilePath;

            // 파일 내용 읽기
            return new String(Files.readAllBytes(Paths.get(fullPath)));
        }
    }

    static class MockServletRequest implements ServletRequest {
        private final Map<String, Object> attributes = new java.util.HashMap<>();

        @Override
        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return java.util.Collections.enumeration(attributes.keySet());
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return new String[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object o) {
            attributes.put(name, o); // 속성 저장
        }

        @Override
        public void removeAttribute(String name) {
            attributes.remove(name); // 속성 제거
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }

        @Override
        public String getRequestId() {
            return null;
        }

        @Override
        public String getProtocolRequestId() {
            return null;
        }

        @Override
        public ServletConnection getServletConnection() {
            return null;
        }
    }

    // Mock ServletResponse (테스트용)
    static class MockServletResponse implements ServletResponse {
        @Override
        public void setContentType(String type) {

        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String charset) {

        }

        @Override
        public void setContentLength(int len) {

        }

        @Override
        public void setContentLengthLong(long len) {

        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void setBufferSize(int size) {

        }

        @Override
        public void flushBuffer() throws IOException {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void resetBuffer() {

        }

        @Override
        public void setLocale(Locale loc) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(System.out); // 콘솔에 출력하도록 설정
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }
    }
}
