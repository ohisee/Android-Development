/**
 * 
 */
package org.freeman.concurrency;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 *
 *
 */
public class TestPerformance {

  private static final String URL =
      "http://localhost:8080/gsf/icloud/businessareas/subscription/subjectareas/sales/vcubes/3555/timeseries";

  private static final String INPUT = "{ \"limit\": 500, \"interval\": "
      + " { \"key\": \"Day\", \"startDate\": \"2017-03-14T00:00:00.000Z\", \"endDate\": \"2017-03-14T00:00:00.000Z\" }, "
      + " \"measures\": [ { \"key\": \"SUBSCRIBER_CNT_im_sub_event_agg_v1\" } ], \"group\": [ \"SUB_TYPE_NAME_im_sub_event_agg_v1\" ], "
      + " \"filters\": [{ \"dimensionKey\": \"SUB_TYPE_NAME_im_sub_event_agg_v1\", "
      + " \"optionKeys\": [\"50GB\",\"200GB\",\"Match\",\"20GB\",\"1000GB\"] }], \"sorting\": \"DESC\" }";

  private static final RestTemplate restTemplate = new RestTemplate();

  private static final MappingJackson2HttpMessageConverter jsonHttpMessageConverter =
      new MappingJackson2HttpMessageConverter();

  static class ResponseMapType extends HashMap<String, Object> {
    private static final long serialVersionUID = 7137327223893287398L;
  }

  static {
    jsonHttpMessageConverter.setSupportedMediaTypes(
        Arrays.asList(new MediaType("application", "json", StandardCharsets.UTF_8)));
    restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
  }

  public static void run(String json, String cube) {
    try {
      String name = Thread.currentThread().getName();
      System.out.println(String.format("[%1$s] : START %2$s at %3$tH:%3$tM:%3$tS", name, cube,
          System.currentTimeMillis()));
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity<String> request = new HttpEntity<String>(json, httpHeaders);
      String re = restTemplate.postForObject(URL, request, String.class);
      System.out.println(String.format("\n[%1$s] : DONE %2$s at %3$tH:%3$tM:%3$tS : %4$s", name,
          cube, System.currentTimeMillis(), re));
    } catch (Exception e) {
      System.out.println(String.format("Cube %s : error %s", cube, e.getMessage()));
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    class Producer implements Callable<String> {
      @Override
      public String call() throws Exception {
        String name = Thread.currentThread().getName();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<String>(INPUT, httpHeaders);
        String re = restTemplate.postForObject(URL, request, String.class);
        System.out.println(String.format("\n[%1$s] : DONE at %2$tH:%2$tM:%2$tS : %3$s", name,
            System.currentTimeMillis(), re));
        int lastIndex = re.indexOf("\"3555\"}") + "\"3555\"}".length();
        return String.format("%s ...", re.substring(0, lastIndex));
      }
    }

    System.out.println("Start");

    List<Callable<String>> producers = new ArrayList<>();

    for (int i = 0; i < 60; i++) {
      producers.add(new Producer());
    }

    try {
      List<Future<String>> futures = executorService.invokeAll(producers);
      futures.forEach(future -> {
        try {
          System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }
  }

}
