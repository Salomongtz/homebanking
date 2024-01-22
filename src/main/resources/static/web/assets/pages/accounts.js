const { createApp } = Vue

createApp({
    data() {
        return {
            client: undefined,
            accounts: [],
        }
    },
    created() {

        this.loadData()
    },
    methods: {
        loadData() {
            console.log("LoadData");
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts.toSorted((a, b) => (a.creationDate) - (b.creationDate))
                    console.log(this.client)
                    console.log(this.accounts);
                })
                .catch(error => {
                    console.log(error)
                })
        },
        createAccount() {
            Swal.fire({
                title: "Choose the account type",
                text: "Choose between saving and checking",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Savings",
                cancelButtonText: "Checking"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/clients/current/accounts?type=SAVINGS")
                        .then(response => {
                            console.log(response)
                            this.loadData()
                            Swal.fire({
                                title: "Created!",
                                text: "Your SAVINGS account has been created.",
                                icon: "success"
                            })
                        })
                        .catch(error => {
                            Swal.fire({
                                title: 'Error!',
                                text: error.getMessage(),
                                icon: 'error',
                                confirmButtonText: 'Go back'
                            })
                            console.log(error)
                        })
                }
                else if (result.dismiss === Swal.DismissReason.cancel) {
                    axios.post("/api/clients/current/accounts?type=CHECKING")
                        .then(response => {
                            console.log(response)
                            this.loadData()
                            Swal.fire({
                                title: "Created!",
                                text: "Your CHECKING account has been created.",
                                icon: "success"
                            })
                        })
                        .catch(error => {
                            Swal.fire({
                                title: 'Error!',
                                text: error.getMessage(),
                                icon: 'error',
                                confirmButtonText: 'Go back'
                            })
                            console.log(error)
                        })
                }
            });

        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")