# How this html page was created
Used chatgpt with the prompt

```
Create an html page that has a welcome text saying "hello visitor" with a form that asks the result of a math operation of adding two random digits. Make sure the user cannot submit the form unless he answers the correct answer.

After pressing the button use AJAX to call a demo http endpoint using Post with a json with one field and then parse the result json, which contains four fields: "value1", "value2", "value3" and "value4" and display them in a nice formatting like a table. The last value is a link so please make sure it is clickable.

Please use only html, css and javascript technologies, use jquery and bootstrap if you need to. Do not use React or any web framework.

Be sure the web page has a responsive design, uses a warm friendly color palette that follows accesibility contrast guidelines. The whole page and html controls must follow WCAG accessibility standards so that it can be managed by people using screen readers.
```

After that, debug and fix to make it work and added the actual real API URL