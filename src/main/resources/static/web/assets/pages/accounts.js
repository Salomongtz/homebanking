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
                    this.accounts = this.client.accounts.toSorted((a, b) => (a.id) - (b.id))
                    console.log(this.client)
                    console.log(this.client.accounts);
                })
                .catch(error => console.log(error))
        },logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")