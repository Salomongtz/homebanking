const { createApp } = Vue

createApp({
    data() {
        return {
            email: "",
            password: "",
            firstName: "",
            lastName: "",
            rememberMe: false
        }
    },
    created() {
        // axios.post('/api/login', "email=david@admin.com&password=***REMOVED***", { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => console.log('signed in!!!'))
        // axios.post('/api/clients', "firstName=pedro2&lastName=rodriguez&email=pedro@mindhub.com&password=pedro", { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => console.log('registered'))
    },
    methods: {
        login() {
            console.log(this.email)
            console.log(this.password)
            axios.post("/api/login?email=" + this.email + "&password=" + this.password)
                .then(response => {
                    console.log('Signed in! Welcome' + this.email + response.data)
                    this.clearData()
                }).catch(error => console.log(error))
            // window.location.href = "/web/assets/pages/accounts.html"
        },
        register() {
            axios.post("/api/clients?firstName=" + this.firstName + "&lastName" + this.lastName + "&email=" + this.email + "&password" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => console.log('registered')).then(this.clearData()).then(this.login())
        },
        clearData() {
            this.email = ""
            this.password = ""
            this.firstName = ""
            this.lastName = ""
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        }
    },
    computed: {
        test() {
            console.log(this.email);
            console.log(this.password);
        }
    }
}
).mount("#app")