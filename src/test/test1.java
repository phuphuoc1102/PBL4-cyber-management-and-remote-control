package test;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class test1 {

    public static void main(String[] args) {
        try {
            // Địa chỉ trang web bạn muốn chặn truy cập
            String blockedWebsite = "http://animevietsub.fan";

            // Thiết lập proxy để chặn truy cập
            blockWebsiteWithProxy(blockedWebsite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void blockWebsiteWithProxy(String website) {
        // Kiểm tra cấu hình proxy trên hệ thống
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        // Tạo đối tượng Proxy nếu có proxy cấu hình
        Proxy proxy = (proxyHost != null && proxyPort != null)
                ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)))
                : Proxy.NO_PROXY;

        // Đặt ProxySelector để sử dụng proxy nếu có
        ProxySelector.setDefault(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                // Nếu URL chứa trang web bị chặn, sử dụng proxy nếu có
                if (uri.toString().contains(website)) {
                    return List.of(proxy);
                } else {
                    return List.of(Proxy.NO_PROXY);
                }
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }
}
