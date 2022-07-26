//Database client
const {Pool} = require('pg');
//Env variables
require('dotenv').config();

//Database connection
const pool = new Pool({
  // Heroku
  connectionString: process.env.DATABASE_URL,
  ssl: {
    rejectUnauthorized: false
  }

  // Local
  // user: process.env.DB_USER,
  // host: process.env.DB_HOST,
  // database: process.env.DB_NAME,
  // password: process.env.DB_PASSWORD,
  // port: process.env.DB_PORT
});

module.exports = pool;