[![Release](https://jitpack.io/v/umjammer/ACD-JAPI.svg)](https://jitpack.io/#umjammer/ACD-JAPI)
[![Java CI with Maven](https://github.com/umjammer/ACD-JAPI/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/ACD-JAPI/actions)
[![CodeQL](https://github.com/umjammer/ACD-JAPI/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/ACD-JAPI/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)
[![Parent](https://img.shields.io/badge/Parent-vavi--apps--fuse-pink)](https://github.com/umjammer/vavi-apps-fuse)

# ACD-JAPI
## Amazon Cloud Drive Java API (unofficial)
This project provide a Java version of Amazon Cloud Drive API from REST version.

## Maven Repository

Maven/Gradle

https://jitpack.io/#umjammer/ACD-JAPI

## Usage
### Configure file

There are two ways to create `Configure` instance.

1. Copy `ACD-JAPI.conf.sample` to `ACD-JAPI.conf` and load by `Configure.load(File configureFile)`.
2. Create a `Configure` instance and update it.

#### Important

name, client_id, client_secret is necessary!

owner is required when use `Property` operation.

Follow HowTo section to get `access_token` and `refresh_token`

#### Field Description
    redirect_uri        #string     after authenticated, redirect the result to this url.
    name (*)            #string     configure file name
    client_secret (*)   #string     secret profile password
    autoRefresh         #boolean    auto refresh token when expired.(token lifetime is 3600)
    owner               #string     this field is required only on `Property` operation.
    client_id (*)       #string     secret profile id
    token_type (*)      #string     API token type (it should be `bearer`, it'll auto refresh when refresh token)
    access_token (**)   #string     API access token.
    writable            #boolean    is Amazon Cloud Drive writable.
    refresh_token (**)  #string     API refresh token.
    autoConfigureUpdate #boolean    auto refresh configure file.
(*) is necessary.

(**) for all API operation. 
### HowTo
Sample run Class is `example.MainSampleClass`

To start using it, you should have amazon account and request a secret profile then add to CloudDrive whitelist.
Please visit [Getting Started](https://developer.amazon.com/public/apis/experience/cloud-drive/content/getting-started#register) for more detail.

1. Add [Security Profile](https://developer.amazon.com/lwa/sp/overview.html)
2. Add your security profile to [Whitelist](https://developer.amazon.com/cd/sp/overview.html)
3. Login to **https://www.amazon.com/ap/oa?client_id=replace_this_with_your_client_id&scope=clouddrive%3Aread%20clouddrive%3Awrite&response_type=code&redirect_uri=http://localhost** 
and get return param **code** from return url. Example: if the return url is 
**http://localhost/?code=ANdNAVhyhqirUelHGEHA&scope=clouddrive%3Aread+clouddrive%3Awrite** , then the **code** is **ANdNAVhyhqirUelHGEHA**.
4. Setting `Configure` by load from file (`Configure.load(File configureFile)`), or create a `Configure` instance and update it.
5. Use `ACDSession.getACDSessionByCode(Configure configure, String code)` to get first `ACDSession`, use `getToken()` to get token or 
it'll save to file if `autoConfigureUpdate` is `true`.
6. Use `ACDSession.getACDSessionByToken(Configure configure, ACDToken acdToken)` to get `ACDSession` when you have previous token.
7. Once you have `access_token` and `refresh_token`, save it at configure file, it can be loaded by `Configure.load(File configureFile)`,
 and update the configure to file by `Configure.save()`. If the `autoConfigureUpdate` and `autoRefresh` field in configure file is set 
 as true, it'll auto refresh token when expired and auto write config to file.

#### The Flow:
                Yes
    Have token--------> Follow 4. and 6. to get ACDSession
        |       
     No |--> Follow 1. ~ 5. to get ACDSession and token
    

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
