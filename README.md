# hotel-booking-app

The application can be configured through the following environment variables:

```properties
IMAGES_FOLDER_PATH=<full path to the directory>
APPLICATION_BASE_URL=<URL where the application runs>
APPLICATION_EMAIL=<email address of the application>
APPLICATION_EMAIL_PASSWORD=<password for the email account>
DATASOURCE_USERNAME=<spring datasource username>
DATASOURCE_PASSWORD=<spring datasource password>
ADMIN_USERNAME=<application admin username>
ADMIN_PASSWORD=<application admin password>
```

* `IMAGES_FOLDER_PATH` Full path of the directory where images will be uploaded. Without any trailing slash or backslash character. Example: `/home/hotelbooking/images` or `D:\\hotelbooking\\images`
* `APPLICATION_BASE_URL` The URL where the application can be accessed. Example: `http://localhost:8080/`

Initial data can be loaded into the database for presentation purposes. To do this, add the following environment variables: `HIBERNATE_DDL_AUTO=update` and `SQL_INIT_MODE=always`.
