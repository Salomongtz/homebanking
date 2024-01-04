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
            axios.post("/api/clients/current/accounts")
                .then(response => {
                    console.log(response)
                    this.loadData()
                    Swal.fire({
                        title: 'Success!',
                        text: 'Your new account has been created!',
                        icon: 'success',
                        confirmButtonText: 'Cool'
                    })
                })
                .catch(error => {
                    Swal.fire({
                        title: 'Error!',
                        text: 'You have reached the maximum number of accounts',
                        icon: 'error',
                        confirmButtonText: 'Go back'
                    })
                    console.log(error)
                })
        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")