module.exports = {
  env: {
    browser: true,
    es2021: true,
    jest: true
  },
  extends: [
    'plugin:react/recommended',
    'airbnb',
  ],
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 'latest',
  },
  plugins: [
    'react',
  ],
  rules: {
    "semi": [1, "never"],
    "indent": ["error", 4],
    "react/jsx-indent" : ["error", 4],
    "react/jsx-indent-props": ["error", 4],
    "react/require-default-props": "off",
    "react/jsx-no-bind": "off",
    "react/forbid-prop-types": "off"
  }
  ,
};
