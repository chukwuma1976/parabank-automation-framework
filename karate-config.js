function fn() {
    var env = karate.env || 'dev';
    var config = {
        baseUrl: 'https://parabank.parasoft.com/parabank/services/bank'
    };
    return config;
}