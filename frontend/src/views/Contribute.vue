<template>
  <div>
    <h1 class="my-4 text-left">Contribute To RateNUS!</h1>
    <v-divider></v-divider>
    <img
      class="mt-6"
      :height="imageHeight"
      src="@/assets/customer-service.png"
    />
    <h1>Contact us</h1>
    <br>
    <p class="subtitle">
        Join Our Team
    </p>
    <p>Want to make an impact on the life of all NUS students?</p>
    <p>Join our team and contribute to our website!</p>
    <p>Talents we are looking for:</p>
    <p>Administrators, Editors, Photographers, Programmers</p>
    <p>If you are interested, please send your CV to ratenus.info@gmail.com</p>
    <br>
    <p class="subtitle">
        Request to add new content
    </p>
    <p>Cannot find a place on our website? Email us and we will add the place for you!</p>
    <p>Please send the information about the hostel/stall/study area in NUS that you want RateNUS to include to ratenus.info@gmail.com</p>
    <br>
    <p class="subtitle">
        Submit a complaint
    </p>
    <p>If you spot any inappropriate content/comment on our website or find any feature hard to use, </p>
    <p>submit a complaint to ratenus.info@gmail.com</p>
    <br>

    <img
      class="mt-6"
      :height="imageHeight"
      src="@/assets/coding.png"
    />
    <h1>Contribute Code</h1>
    <br>
    <p class="subtitle">
        Pull Request
    </p>
    <p>Please create a GitHub pull request if you want to improve our code.</p>
    <div class="link" @click="redirectToGitHubPRs()">Create a pull request here</div>
    <p>We will review your request and update the changes to our website if accepted!</p>
    <br>
    <p class="subtitle">
        Report Bug
    </p>
    <p>If you spot a bug in our website, please report it here:</p>
    <div class="link" @click="redirectToGitHubIssues()">Report a bug here</div>
    <p>Alternatively you can send the description of the bug through email to ratenus.info@gmail.com</p>
    <p>We will fix the bug as soon as possible.</p>

    <br>
    <br>
    <div class="attribute" @click="redirectToContactUsIcon()">Contact us icons created by alimasykurm - Flaticon</div>
    <div class="attribute" @click="redirectToCodeIcon()">Code icons created by prettycons - Flaticon</div>
  </div>
</template>

<script>
import GithubRequest from "../httpRequests/GithubRequest.js";
export default {
    computed: {
        imageHeight() {
          switch (this.$vuetify.breakpoint.name) {
            case "xs":
              return 50;
            case "sm":
              return 70;
            case "md":
              return 80;
            case "lg":
              return 90;
            case "xl":
              return 100;
            default:
              return 80;
          }
        },
    },
    methods: {
        redirectToGitHubPRs() {
            window.open(GithubRequest.getPullRequestUrl());
        },
        redirectToGitHubIssues() {
            window.open(GithubRequest.getIssuesUrl());
        },
        redirectToContactUsIcon() {
            window.open("https://www.flaticon.com/free-icons/contact-us");
        },
        redirectToCodeIcon() {
            window.open("https://www.flaticon.com/free-icons/code");
        }
    }
};
</script>

<style>
    .subtitle {
        font-size: 1.3em;
        font-weight: bold
    }
    .attribute {
        font-size: 0.7em
    }
    .attribute:hover {
      color: #ff6d00;
      cursor: pointer;
    }
</style>
