var express = require('express'),
    serveStatic = require('serve-static'),
    swaggerUi = require('swagger-ui-express'),
    swaggerDocument = require('./doc/swagger.json');
var app = express();
app.use(serveStatic('public', {'index': ['index.html']}))
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
app.listen(parseInt(process.argv.slice(2)));