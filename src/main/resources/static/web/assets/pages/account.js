const { createApp } = Vue

createApp({
    data() {
        return {
            account: {},
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
            axios.get('/api/accounts/'+queryParams.get('id'))
                .then(response => {
                    this.account = response.data
                    this.transactions = this.account.transactions
                    this.transactions.sort((a, b) => (b.id) - (a.id))
                    console.log(this.account)
                    console.log(this.account.transactions);
                })
                .catch(error => console.log(error))
        }
    }
}).mount("#app")