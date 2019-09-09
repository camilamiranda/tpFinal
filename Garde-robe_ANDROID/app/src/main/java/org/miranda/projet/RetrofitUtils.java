package org.miranda.projet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class RetrofitUtils {

    public static Service get(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(getClient())
                .build();

        Service service = retrofit.create(Service.class);
        return service;
    }

    public static class MyCookieJar implements CookieJar {

        private List<Cookie> cookies;

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            this.cookies = cookies;
            if(ListCookie.listCookie == null){
                for (Cookie c:this.cookies) {
                    ListCookie.addCookie(c);
                }
            }
            else{
                ListCookie.addCookie(cookies.get(cookies.size() - 1));
            }

        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> res = new ArrayList<>();
            if(ListCookie.getListCookie() != null){
                for (Cookie c:ListCookie.getListCookie()) {
                    res.add(c);
                }
            }

            if (cookies != null){
                for(Cookie c : cookies){
                    if (c.expiresAt() > System.currentTimeMillis()) {
                        res.add(c);
                    }
                }
            }
            return res;
        }
    }

    public static OkHttpClient getClient(){
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // Sets the cookie Jar to automatically handles incoming and outgoing cookies
            CookieJar cookieJar = new MyCookieJar();
            builder = builder.cookieJar(cookieJar);



            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // configure the builder to accept all SSL certificates
            builder = builder.sslSocketFactory(sslSocketFactory);
            // configure the builder to accept all hostnames includint localhost
            builder = builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            // Adds logging capability to see http exchanges on Android Monitor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder = builder.addInterceptor(interceptor);
            return builder.build();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    public static OkHttpClient getClient(){
//        try {
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            // Sets the cookie Jar to automatically handles incoming and outgoing cookies
//
//            // Adds logging capability to see http exchanges on Android Monitor
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder = builder.addInterceptor(interceptor);
//            return builder.build();
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }


//    public static Service getMock(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.github.com/users/jorisdeguet/repos/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        NetworkBehavior networkBehavior = NetworkBehavior.create();
//        networkBehavior.setDelay(1000, TimeUnit.MILLISECONDS);
//        networkBehavior.setVariancePercent(90);
//
//        MockRetrofit mock = new MockRetrofit.Builder(retrofit)
//                .networkBehavior(networkBehavior)
//                .build();
//
//        BehaviorDelegate<Service> delegate =
//                mock.create(Service.class);
//
//        return new ServiceMock(delegate);
//    }
}
