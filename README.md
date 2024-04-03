# hotel-booking-app

`.env` file needs to be present in the root directory with the following environment variables:

```properties
IMAGES_FOLDER_PATH=<full path to the directory>
```

* `IMAGES_FOLDER_PATH` Full path of the directory where images will be uploaded. Without any trailing slash or backslash character. Example: `/home/hotelbooking/images` or `D:\\hotelbooking\\images`

Initial data can be loaded into the database for presentation purposes. To do this, add the following environment variable to the `.env` file: `SQL_INIT_MODE=always` and place
the `data.sql` population script in the root directory.
