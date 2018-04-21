package project.iti.Injection;

/**
 * Created by asmaa on 02/26/2018.
 */

import project.iti.Data.Repository.HomeRepository;
import project.iti.Data.Repository.NetworkHomeRepository;

/**
 *  injection class to manage my contexts to start specific repository to get data from api request
 *  or local connection Database like sqlLite to  continue the sequence
 *  "start repository >> get data >> presenter >> set data in view"
 */
public class Injection {

    public static HomeRepository provideHomeRepository(){
        return new NetworkHomeRepository();
    }

}
