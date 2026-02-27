package com.example.defectservice.service;

import com.example.defectservice.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CollectService {

    @Value("${python.collect.base-url}")
    private String pythonBaseUrl;   // 例如：http://localhost:8000

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 调用 Python 的缺陷采集接口
     */
    public String collectIssueFromPython(String owner,
                                         String repo,
                                         String state,
                                         String platform,
                                         String since,
                                         String until,
                                         String repoId) {

        String url = pythonBaseUrl + "/api/collect/issue";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("owner", owner)
                .queryParam("repo", repo)
                .queryParam("state", state)
                .queryParam("platform", platform)
                .queryParam("since", since)
                .queryParam("until", until)
                .queryParam("repo_id", repoId);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new BusinessException("调用采集服务失败，状态码：" + response.getStatusCode());
            }
            return response.getBody();

        } catch (Exception e) {
            throw new BusinessException("调用 Python 采集接口异常：" + e.getMessage());
        }
    }
}
