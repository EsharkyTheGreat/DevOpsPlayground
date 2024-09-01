const express = require("express");
const dotnev = require("dotenv");

const { v4: uuidv4 } = require("uuid");
const service_uuid = uuidv4();

dotnev.config();

const app = express();
const PORT = 3000;

app.get("/", (req, res) => {
  const service = process.env.SERVICE_NAME;
  res.send(`Hello from ${service}, ServiceID: ${service_uuid}`);
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
