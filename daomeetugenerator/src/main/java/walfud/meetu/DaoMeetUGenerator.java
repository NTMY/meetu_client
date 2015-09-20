package walfud.meetu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DaoMeetUGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "walfud.meetu.model");

        // User
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();
        Property userId = user.addLongProperty("userId").getProperty();
        user.addStringProperty("phoneNum");
        user.addStringProperty("password");
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

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}
