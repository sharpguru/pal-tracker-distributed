package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    ConcurrentMap ProjectInfoCache;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
        ProjectInfoCache = new ConcurrentHashMap();
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        //return restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class);
        ProjectInfo pi = restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class);
        ProjectInfoCache.put(projectId, pi);
        System.out.println("Added Project to cache-"+projectId);
        return pi;
    }

    public ProjectInfo getProjectFromCache(long projectId) {

        ProjectInfo projectInfo = (ProjectInfo)ProjectInfoCache.get(projectId);
        if(projectInfo != null)
            System.out.println("Got Project to cache-"+projectId);
        else
            System.out.println("No Project in cache with ID -"+projectId);
        return projectInfo;
    }

}
