package Common;

import java.util.List;

public class UsersPropertiesList {


    private List<UserProperties> userProperties;

    public UsersPropertiesList(List<UserProperties> userProperties) {

        this.userProperties = userProperties;
    }

    public UserProperties find(String text) {
        if (text == null || text.isEmpty()) {
            return  null;
        }

        int i = 0;
        boolean found = false;
        while ( i < userProperties.size() && !found) {
            if (userProperties.get(i).name.equals(text)){
                found = true;
            } else {
                i++;
            }
        }

        if (found)
            return userProperties.get(i);
        else
            return null;
    }


    public void add(UserProperties prop) {
        userProperties.add(prop);
    }
}
