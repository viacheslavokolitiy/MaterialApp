package org.satorysoft.cotton.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ApplicationRiskUtil {
    private final List<String> permissions;

    public ApplicationRiskUtil(List<String> permissions){
        this.permissions = permissions;
    }

    public double evaluateApplicationRisk(String[] applicationPermissions) {
        if(applicationPermissions == null){
            applicationPermissions = new String[]{};
        }

        List<String> dangerousPermissions = permissions;
        ArrayList<String> foundPermissions = new ArrayList<>();
        ArrayList<String> safePermissions = new ArrayList<>();
        for(String permissionName : applicationPermissions){
            if(dangerousPermissions.contains(permissionName)){
                foundPermissions.add(permissionName);
            } else {
                safePermissions.add(permissionName);
            }
        }

        double riskRate;

        if(safePermissions.size() > 0 || foundPermissions.size() > 0){
            riskRate = (double)safePermissions.size() / (safePermissions.size() + foundPermissions.size());
            safePermissions.clear();
            foundPermissions.clear();
            safePermissions.trimToSize();
            foundPermissions.trimToSize();
        } else {
            riskRate = 0.0;
        }

        return riskRate;
    }
}
