package com.iv1201.recruitmentapplication;

import com.iv1201.recruitmentapplication.model.payload.request.LoginRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class TestUtil {

    private static final String uriTemplate = "http://localhost:%s/%s";


    public static URI localURI(int port ,String path){
        if(Objects.isNull(path))
            path = "";
        else if(path.startsWith("/"))
            path = path.substring(1);

        URI uri = URI.create(String.format(uriTemplate,port,path));
        return uri;
    }

    public static boolean collectionContains(Collection collection , Object...objs){
        return Objects.nonNull(collection) && collection.containsAll(List.of(objs));
    }

}
