require('should');

const zapier = require('zapier-platform-core');

// Use this to make test calls into your app:
const App = require('../index');
const appTester = zapier.createAppTester(App);

describe('creates', () => {

    describe('uploadFile', () => {
        it('should get a result from a pdf file', (done) => {
            const bundle = {
                inputData: {
                    regex: '\\d{2}[A-Z]{2}\\d{4}',
                    pdf: 'https://www.dropbox.com/s/e58rciktj3d7ojk/test.pdf?dl=1',
                }
            };

            appTester(App.creates.find.operation.perform, bundle)
                .then((result) => {
                    result.should.have.property('data');
                    result.data.should.be.eql("88RF0293");
                    done();
                })
                .catch(done);
        });
    });

});
