import Vue from "vue";
import Vuex from "vuex";
import UserUtil from "../components/authentication/UserUtil";
import createPersistedState from 'vuex-persistedstate'
import SecureLS from "secure-ls";
const ls = new SecureLS({ isCompression: false });

Vue.use(Vuex);

export default new Vuex.Store({
    plugins: [createPersistedState({
        storage: {
            getItem: key => ls.get(key),
            setItem: (key, value) => ls.set(key, value),
            removeItem: key => ls.remove(key)
          }
    })],
    state: {
        isLoggedIn: false,
        user: {
            fullName: "",
            initials: "",
            email: "",
            jwtToken: ""
        }
    },
    //Vuex getter properties are available to components on the store.getters object.
    //console.log(this.$store.getters.firstName);
    getters: {
        fullName: state => {
            return state.user.fullName;
        },
        initials: state => {
            return state.user.initials;
        },
        email: state => {
            return state.user.email;
        },
        jwtToken: state => {
            return state.user.jwtToken;
        },
        isLoggedIn: state => {
            return state.isLoggedIn;
        }
    },
    mutations: {
        //call this method from our component using the store.commit method, with our payload as the second argument.
        //this.$store.commit("changeName", "New Name");
        changeName(state, payload) {
            state.user.fullName = payload;
            state.user.initials = UserUtil.getInitials(payload);
        },
        changeEmail(state, email) {
            state.user.email = email
        },
        updateJwtToken(state, token) {
            state.user.jwtToken = token;
        },
        logIn(state) {
            state.isLoggedIn = true;
        },
        logOut(state) {
            state.isLoggedIn = false;
            state.user.jwtToken = "";
        }
        
    },
    actions: {}
});