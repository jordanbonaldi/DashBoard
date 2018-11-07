const m_path = require('path')
const m_uglifier = require('uglifyjs-webpack-plugin')
const m_cleaner = require('clean-webpack-plugin')
const m_extracter = require("extract-text-webpack-plugin")
const m_manifest = require('webpack-manifest-plugin')

const is_dev = process.env.NODE_ENV === "dev"

let m_css_conf = [
  {
    loader: 'css-loader',
    options: {
      minimize: !is_dev,
      importLoaders: 1
    }
  }
]

let m_cleaner_path = [
  'build',
  './public/assets/'
]

let m_cleaner_opts = {
  root: m_path.resolve('./'),
  verbose:  true,
  dry:      false
}

let m_config = {
  entry: {
    hcc: ['./assets/scss/app.scss', './assets/app.js']
  },
  watch: is_dev,
  devtool: is_dev ? "cheap-module-eval-source-map" : false,
  output: {
    filename: is_dev ? '[name].js' : '[name].js',
    path: m_path.resolve('./public/assets'),
    publicPath: '/assets/'
  },
  resolve:{
    alias: {
      '@css': m_path.resolve('./assets/css/'),
      '@images': m_path.resolve('./assets/images/'),
      '@js': m_path.resolve('./assets/js/'),
      '@vue': m_path.resolve('./assets/vues/'),
      '@module': m_path.resolve('./node_modules/'),
      '@fw': m_path.resolve('./semantic/dist/')
    }
  },
  module: {
     rules: [
      //  {
      //    test: /\.js$/,
      //    enforce: 'pre',
      //    exclude: /(node_modules|bower_components)/,
      //    use: ['eslint-loader']
      //  },
       {
         test: /\.js$/,
         exclude: /(node_modules|bower_components)/,
         use: {
				loader: 'babel-loader',
			}
       },
       {
         test: /\.vue$/,
         loader: 'vue-loader',
         options: {
           loaders: {
             js: 'babel-loader',
             sass: 'vue-style-loader!css-loader!sass-loader?indentedSyntax',
             scss: 'vue-style-loader!css-loader!sass-loader',
           }
         }
       },
       {
         test: /\.scss$/,
         use: m_extracter.extract({
          fallback: "style-loader",
          use: [...m_css_conf, 'sass-loader']
        })
      },
       {
         test: /\.css$/,
         use: m_extracter.extract({
           fallback: "style-loader",
           use: [...m_css_conf, 'sass-loader']
         })
       },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'file-loader'
      },
      {
        test: /\.(png|jpe?g|gif|svg)$/i,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 8192
            }
          },
          {
            loader: 'img-loader',
            options: { enabled: !is_dev }
          }
        ]
      }
     ]
   },
   plugins: [
     new m_extracter({
       filename: is_dev ? '[name].css' : '[name].css'
     }),
     new m_manifest(),
     new m_cleaner(m_cleaner_path, m_cleaner_opts)
   ]
}

if (!is_dev) m_config.plugins.push(new m_uglifier())

module.exports = m_config;
