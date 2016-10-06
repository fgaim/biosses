package services;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by T082123 on 22.09.2016.
 */
@WebService
public class SSESService {

    @WebMethod
    public String calculateSimilarityScoreForGivenPair(String s1, String s2, int methodType){
        String score="";

        switch (methodType){

            case 1:
                //combined ontology method
                break;
            case 2:
                //qgram
                break;
            case 3:
                //paragraph vector
                break;
            case 4:
                //supervised
                break;
        }
        return score;
    }
}
