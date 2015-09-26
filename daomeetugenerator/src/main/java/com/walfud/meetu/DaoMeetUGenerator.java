package com.walfud.meetu;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DaoMeetUGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.walfud.meetu.database");

        // User
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();
        Property userId = user.addLongProperty("userId").getProperty();
        user.addStringProperty("password");
        user.addStringProperty("nick");
        user.addStringProperty("mood");
        user.addStringProperty("portrait");
        user.addStringProperty("phoneNum");
        user.addStringProperty("imei");

        // Location
        Entity location = schema.addEntity("Location");
        Property locationId = location.addIdProperty().autoincrement().getProperty();
        location.addDoubleProperty("longitude");
        location.addDoubleProperty("latitude");
        location.addStringProperty("address");
        location.addStringProperty("business");
        location.addDateProperty("uploadTime");
        location.addLongProperty("userId");

        // Foreign key
//        user.addToMany(location, userId);
//        location.addToOne(user, userId);

        new File("./app/build/generated/greendao").mkdir();
        new DaoGenerator().generateAll(schema, "./app/build/generated/greendao");
    }
}
