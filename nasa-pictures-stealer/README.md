## Task

### Build a Nasa Pictures Stealer

- **Create a new web app**
   - Build a stealing command HTTP endpoint
   - Handle a POST request by path ../pictures/steal
   - Request body example `{ "sol": 14 }`
   - Call a corresponding service to trigger stealing pictures

- **Implement a Service Layer**
  - Call NASA API and receive pictures data
  - Parse the response to fetch the information about cameras and pictures
  - Store the given data using the persistence layer

- **Implement a Persistence Layer**
  - Connect to a local database
  - Create a schema to store the data
  ```
   cameras(id, nasa_id, name, created_at)
   pictures(id, nasa_id, img_src, camera_id, created_at)
   ```

## Connecting to local database

1. Make sure you have Docker installed on your local machine
2. Run the following commands:
   - Pull the Postrges Docker image: `docker pull postgres`
   - Create and run the Postgres container: `docker run -d --name bobocode-database -p 5432:5432 -e POSTGRES_PASSWORD=password postgres`
3. Now that you have Postgres running on `localhost:5432` you can connect to it from IntelliJ IDEA
4. After connecting to your local database you can open a Query Console from IntelliJ IDEA and run queries in order to create a database and tables. 
You can find queries used to make this homework in the `scripts.sql` file in resources folder.



