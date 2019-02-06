var express = require('express'),
    serveStatic = require('serve-static'),
    swaggerUi = require('swagger-ui-express'),
    swaggerDocument = require('./doc/swagger.json');
var app = express();
app.use(serveStatic('public', {'index': ['index.html']}))
var options = {
  customCss: '.swagger-ui .topbar { display: none }',
  customSiteTitle: 'GeoQuotient API'
};
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument, options));
app.listen(parseInt(process.argv.slice(2)));