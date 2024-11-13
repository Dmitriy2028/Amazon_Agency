print('Start #################################################################');

// init-db.js
const fs = require('fs');
const path = '/docker-entrypoint-initdb.d/test_report.json';

// Считываем JSON файл
const reports = JSON.parse(fs.readFileSync(path, 'utf8'));

// Получаем доступ к базе данных 'reports' и коллекции 'testReports'
const db = db.getSiblingDB('reports');
const collection = db.testReports;

// Вставляем данные из JSON в коллекцию
collection.insertMany(reports);
print('Data inserted successfully');

print('END #################################################################');