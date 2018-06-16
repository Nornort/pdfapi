require('should');

const zapier = require('zapier-platform-core');

// Use this to make test calls into your app:
const App = require('../index');
const appTester = zapier.createAppTester(App);

describe('PDFApi', () => {

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


    describe('split file', () => {
        it('should get a result from a pdf file', (done) => {
            const bundle = {
                inputData: {
                    pages: '1,2-3,1-3',
                    pdf: 'https://www.dropbox.com/s/e58rciktj3d7ojk/test.pdf?dl=1',
                }
            };

            appTester(App.creates.split.operation.perform, bundle)
                .then((result) => {
                    result.should.have.property("element0");
                    result.should.have.property("element1");
                    result.should.have.property("element2");
                    result["element0"].should.have.property("file");
                    done();
                })
                .catch(done);
        });
    });

});
