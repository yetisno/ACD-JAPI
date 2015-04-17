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

`ACD` provide friendly method to operate those API.

You should create a `ACDSession` instance for all operation.

The raw APIs are at `org.yetiz.lib.acd.api` package.

**Support** log4j (using slf4j)