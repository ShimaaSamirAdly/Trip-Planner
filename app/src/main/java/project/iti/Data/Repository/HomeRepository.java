package project.iti.Data.Repository;

import project.iti.Listener.OnHomeResult;

/**
 * Created by asmaa on 02/27/2018.
 */

/**
 * interface repository to write the method will be use in repository to handle all repositories
 */
public interface HomeRepository {
    void getHomeData( OnHomeResult onHomeResult);
    }


