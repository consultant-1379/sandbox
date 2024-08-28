(function () {
    bps.service('templatesModel', function () {
    this.get = function () {
        return [
            {
                name: "T1",
                description: "T1 Description"
            },
            {
                name: "T2",
                description: "T2 Description",
                config: [
                    {tag:"checkbox", id:"check1", label:"Yes/No"},
                    {tag:"input", id:"input1", label:"Entry 1"},
                    {tag:"radio", id:"radio1", label:"Radio 1", options:["Yes","No"]},
                    {tag:"textarea", id:"input2", label:"Entry 2"},
                    {tag:"radio", id:"radio2", label:"Radio 2", options:["1","2"]}
               ]
            },
            {
                name: "T3",
                description: "T3 Description"
            }
        ];
    };
});
})();