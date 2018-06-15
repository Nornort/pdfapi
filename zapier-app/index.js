
const FormData = require('form-data');
const request = require('request');

// We can roll up all our behaviors in an App.
const App = {
    // This is just shorthand to reference the installed dependencies you have. Zapier will
    // need to know these before we can upload
    version: require('./package.json').version,
    platformVersion: require('zapier-platform-core').version,

    authentication: { // No auth
        type: "custom",
        test: () => {}
    },

    // beforeRequest & afterResponse are optional hooks into the provided HTTP client
    beforeRequest: [],

    afterResponse: [],

    // If you want to define optional resources to simplify creation of triggers, searches, creates - do that here!
    resources: {},

    // If you want your trigger to show up, you better include it here!
    triggers: {},

    // If you want your searches to show up, you better include it here!
    searches: {},

    // If you want your creates to show up, you better include it here!
    creates: {

        find: {
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
    }
};

// Finally, export the app.
module.exports = App;
