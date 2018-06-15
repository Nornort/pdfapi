
module.exports = {
    dlForReference: (z, bundle) => {
        // use standard auth to request the file
        const filePromise = z.request({
            url: `http://pdfapi.philippevienne.com/api/dl/${bundle.inputData.reference}`,
            raw: true
        });
        // and swap it for a stashed URL
        return z.stashFile(filePromise);
    }
};
