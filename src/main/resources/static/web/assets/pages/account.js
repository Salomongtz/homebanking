const { createApp } = Vue

createApp({
    data() {
        return {
            account: {},
            client: {},
            transactions: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            console.log("LoadData");
            const queryParams = new URLSearchParams(window.location.search)
            axios.get('/api/accounts/' + queryParams.get('id'))
                .then(response => {
                    this.account = response.data
                    this.transactions = this.account.transactions
                    this.transactions.sort((a, b) => (b.id) - (a.id))
                    console.log(this.account)
                    console.log(this.account.transactions);
                })
                .catch(error => console.log(error))
            axios.get('/api/clients/1')
                .then(response => {
                    this.client = response.data
                    console.log(this.client)
                })
                .catch(error => console.log(error))
        }, logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")