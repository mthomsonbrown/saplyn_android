package com.slashandhyphen.saplyn.Models;

import com.slashandhyphen.saplyn.Models.Pojo.User;

/**
 * Created by Mike on 9/14/2016.
 */
public class FakeModel {
    private String token = "b1e6668141b3dd7f8b12c13ae38bb78c";
    private User user = new User(token);

    public String getStuffFromModel () {
        return user.successString;
    }
}
