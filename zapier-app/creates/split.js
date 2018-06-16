const FormData = require('form-data');
const request = require('request');
const dlForReference = require("../hydrators").dlForReference;


module.exports = {
    key: 'split', // uniquely identifies the trigger
    noun: 'Split PDF Pages', // user-friendly word that is used to refer to the resource
    // `display` controls the presentation in the Zapier Editor
    display: {
        label: 'Split PDF Pages',
        description: 'Split PDF Pages into multiple pdf files'
    },
    // `operation` implements the API call used to fetch the data
    operation: {
        inputFields: [
            {
                key: 'pages',
                required: true,
                label: 'Page repartition',
                helpText: 'Type pages or range of pages (E.g. "1,2-3,4-8")'
            },
            {
                key: 'pdf',
                required: true,
                type: "file"
            }
        ],
        perform: function (z, bundle) {
            const form = new FormData();
            form.append("pages", bundle.inputData['pages']);
            form.append("pdf", request(bundle.inputData.pdf));
            return z.request({
                url: 'http://pdfapi.philippevienne.com/api/split/',
                method: 'POST',
                body: form
            })
                .then(function (response) {
                    const files = {};
                    response.json.forEach((result, index) => {
                            // lazily convert a secret_download_url to a stashed url
                            // zapier won't do this until we need it
                            result.file = z.dehydrate(dlForReference, {
                                reference: result['reference']
                            });
                            delete result['reference'];
                            result.index = index;
                            files["element" + index] = result;
                        });
                    return files;
                });
        }
    }
}
