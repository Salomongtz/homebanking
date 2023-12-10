const {createApp} = Vue

createApp({
    data() {
        return {
            accounts: []
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios
                .get('/api/accounts')
                .then(response => (this.accounts = response.data))
                .catch(error => console.log(error))
        }
    }
}).mount