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
            const id = new URLSearchParams(window.location.search).get("id")
            
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.account = this.client.accounts.find(account => account.id == id)
                    this.transactions = this.account.transactions   
                    this.transactions.sort((a, b) => (b.id) - (a.id))
                    console.log(this.client)
                    console.log(this.account)
                    console.log(this.account.transactions);
                })
                .catch(error => console.log(error))
        }, logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")