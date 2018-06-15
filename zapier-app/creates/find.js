const FormData = require('form-data');
const request = require('request');

module.exports = {
    key: 'find', // uniquely identifies the trigger
    noun: 'Find an element', // user-friendly word that is used to refer to the resource
    // `display` controls the presentation in the Zapier Editor
    display: {
        label: 'Find an Element',
        description: 'Search a data into your PDF'
    },
    // `operation` implements the API call used to fetch the data
    operation: {
        inputFields: [
            {
                key: 'regex',
                required: true,
                label: 'Regular Expression',
                helpText: 'How to find your data'
            },
            {
                key: 'pdf',
                required: true,
                type: "file"
            }
        ],
        perform: function (z, bundle) {
            const form = new FormData();
            form.append("regex", bundle.inputData.regex);
            form.append("pdf", request(bundle.inputData.pdf));
            return z.request({
                url: 'http://pdfapi.philippevienne.com/api/findUnique/',
                method: 'POST',
                body: form
            })
                .then(function (response) {
                    return response.json;
                });
        }
    }
}
