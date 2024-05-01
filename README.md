# hotel-booking-app

The application can be configured through the following environment variables:

```properties
IMAGES_FOLDER_PATH=<full path to the directory>
APPLICATION_BASE_URL=<URL where the application runs>
APPLICATION_EMAIL=<email address of the application>
APPLICATION_EMAIL_PASSWORD=<password for the email account>
MYSQL_ROOT_PASSWORD=<root password of the mysql database>
DATASOURCE_USERNAME=<spring datasource username>
DATASOURCE_PASSWORD=<spring datasource password>
ADMIN_USERNAME=<application admin username>
ADMIN_PASSWORD=<application admin password>
```

* `IMAGES_FOLDER_PATH` Full path of the directory where images will be uploaded. Without any trailing slash or backslash character. Example: `/home/hotelbooking/images` or `D:\\hotelbooking\\images`. While running inside a container this value should be simply `images` as the Dockerfile is configured to create this folder inside the container for images.
* `APPLICATION_BASE_URL` The URL where the application can be accessed. Example: `http://localhost:8080/`


## Running the application

The application was designed to run as a Docker container. If you haven't installed Docker yet, you can download it from the [official Docker website](https://www.docker.com/) .

Create a file named `.env` with the environment variables mentioned above and place it in the root of the project directory. Then run the application using `docker-compose up`.
Once the containers are up and running, you can access the application by opening a web browser and navigating to `http://localhost:<port>` where `<port>` is the port specified in the docker-compose.yml file for the application. Example: `http://localhost:8080/hotelbooking/home`.

To stop the application use `docker-compose down`.

### Dummy data

Initial data can be loaded into the database for presentation purposes. To do this, add the following environment variables: `HIBERNATE_DDL_AUTO=create` and `SQL_INIT_MODE=always`. The images of hotels are not copied automatically to the volume. After the containers are started with `docker compose up` these images can be copied with `docker-compose exec app sh -c '/app/copy_files.sh'`.

If the command can not find the `copy_files.sh` file, it likely indicates that it's line endings were changed. To fix the issue, open the file in a text editor and change to UNIX style line endings.

This data is saved into the same storage that the live application uses and should be deleted once it's no longer needed.
