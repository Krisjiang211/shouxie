package com.jiang.singlelearningdemo.pvz2;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.print.attribute.standard.JobKOctets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestLimitTimeController {

    LimitTime<AtomicLong> objective = new LimitTime<>(5000,35);

    AtomicLong atomicLong = new AtomicLong(0L);


    @GetMapping("test/limitTime")
    public String testLimitTime(@RequestParam(value = "r",required = false) Boolean isRestart){
        if (isRestart!=null && isRestart){
            objective = new LimitTime<>(5000,35);
            atomicLong = new AtomicLong(0L);
        }
        Item<AtomicLong> tItem = new Item<>(atomicLong, System.currentTimeMillis());
        objective.add(tItem);
        return objective.getProgress();
    }


    @GetMapping("test/limitTime/intimeProgress")
    public Flux<String> testLimitTimeInTimeProgress(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        Sinks.Many<String> sinks=Sinks.many().multicast().onBackpressureBuffer();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(()-> {
                    objective.checkAndRefresh();
                    sinks.tryEmitNext(objective.getProgress()+"\n");
                },
                0,100, TimeUnit.MILLISECONDS);
        return sinks.asFlux();
    }


}
