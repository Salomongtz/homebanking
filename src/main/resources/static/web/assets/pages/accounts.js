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
                    this.accounts = this.client.accounts
                    console.log(this.client)
                    console.log(this.client.accounts);
                })
                .catch(error => console.log(error))
        }
    }
}).mount("#app")