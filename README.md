# ACD-JAPI
## Amazon Cloud Drive Java API (unofficial)
This project provide a Java version of Amazon Cloud Drive API from REST version.

## Usage
### Configure file
1. You can copy ACD-JAPI.conf.sample, name it as ACD-JAPI.conf and setting it.(name, client_id, client_secret is 
required at least!)
2. You can just create `Configure` instance and setting it.

#### Field Description
    redirect_uri #string - after authenticated, redirect the result to this url.
    name #string - configure file name
    client_secret #string - secret profile password
    autoRefresh #boolean - auto refresh token when expired.(token lifetime is 3600)
    owner #string - this field is required only on `Property` operation.
    client_id #string - secret profile id
    token_type #string - API token type (it should be `bearer`, it'll auto refresh when refresh token)
    access_token #string - API access token.
    writable #boolean - is Amazon Cloud Drive writable.
    refresh_token #string - API refresh token.
    autoConfigureUpdate #boolean - auto refresh configure file.

### HowTo
Sample run Class is `org.yetiz.lib.acd.sample.MainSampleClass`

To start using it, you should have amazon account and request a secret profile then add to CloudDrive whitelist.
Please visit [Getting Started](https://developer.amazon.com/public/apis/experience/cloud-drive/content/getting-started#register) for more detail.

1. Add [Security Profile](https://developer.amazon.com/lwa/sp/overview.html)
2. Add your security profile to [Whitelist](https://developer.amazon.com/cd/sp/overview.html)
3. [Login](https://www.amazon.com/ap/oa?client_id=Client_id&scope=clouddrive%3Aread%20clouddrive%3Awrite&response_type=code&redirect_uri=http://localhost) and get return code on url. Example: the return url is 
**http://localhost/?code=ANdNAVhyhqirUelHGEHA&scope=clouddrive%3Aread+clouddrive%3Awrite** , then the code is **ANdNAVhyhqirUelHGEHA**.
4. Use `ACDSession.getACDSessionByCode(Configure configure, String code)` to get first `ACDSession`
5. Once you have `access_token` and `refresh_token`, save it at configure file, it can be loaded by `Configure.load(File configureFile)`,
 and update the configure to file by `Configure.save()`. If the `autoConfigureUpdate` and `autoRefresh` field in configure file is set 
 as true, it'll auto refresh token when expired and auto write config to file.

`ACD` provide friendly method to operate those API.

You should create a `ACDSession` instance for all operation.

The raw APIs are at `org.yetiz.lib.acd.api` package.

### Contributing

Please fork and send Pull Request

Respect the formatting rules:
Ident with 1 tab
Use a 140 chars line max length
Don't use * imports

**Support** log4j (using slf4j)
