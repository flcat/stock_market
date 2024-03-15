import numpy as np
from pandas_datareader import data as web
import pandas as pd
import yfinance as yf
from dateutil.relativedelta import relativedelta
import datetime
import time

import os
from os import listdir
from os.path import isfile, join

import statsmodels.api as sm
import seaborn as sns
from statsmodels.tsa.ar_model import AutoReg, ar_select_order

yf.pdr_override()
end = datetime.datetime.now()
stocks_not_downloaded = []
missing_stocks = []

folder = '/Users/jaechankwon/Downloads/stock_market/python_workspace/stock_list'
files = [x for x in listdir(folder) if isfile(join(folder, x))]
tickers = [os.path.splitext(x)[0] for x in files]
tickers
tickers.sort()
len(tickers)

def save_to_csv_from_yahoo(folder, ticker, syear, smonth, sday, eyear, emonth, eday):
    start = datetime.datetime(syear, smonth, sday)
    end = datetime.datetime(eyear, emonth, eday)

    try:
        print("Get Data for :", ticker)
        # df = web.DataReader(ticker, start, end)["Adj Close"]
        df = web.get_data_yahoo(ticker, start, end)
        # df = yf.download(ticker, start, end)
        # print(df)
        if df.empty:
            print("No Data for :", ticker)
            stocks_not_downloaded.append(ticker)
            pass
        time.sleep(10)
        df.to_csv('{ticker}.csv'.format(ticker=ticker))

    except Exception as ex:
            stocks_not_downloaded.append(ticker)
            print("Could not Get Data for:", ticker)

#Returns a Named Column Data from CSV
def get_column_from_csv(file, col_name):
        try:
            df = pd.read_csv(file)
            if df.empty:
                print("No Data for :", ticker)
                stocks_not_downloaded.append(ticker)
                pass
        except FileNotFoundError:
            print("File Does Not Exist")
        else:
            return df[col_name]

# Returns a Named DataFrame from CSV
def get_df_from_csv(folder, ticker):
    try:
        df = pd.read_csv(folder + ticker + '.csv', index_col='Date', 
                         parse_dates=True)
    except FileNotFoundError:
        pass
        print("File Doesn't Exist")
    else:
        return df

def update_stock_data(ticker):
    up_folder = '/Users/jaechankwon/Downloads/stock_market/python_workspace/stock_list/update/'

    stock_folder = folder

    update_df = get_df_from_csv(up_folder, ticker)
    update_df

    # Get original dataframe
    orig_df = get_df_from_csv(stock_folder, ticker)

    # Drop the unnamed column
    orig_df.drop(orig_df.columns[orig_df.columns.str.contains('unnamed',case = False)],
          axis = 1, inplace = True)

    # Drop the daily_return column
    orig_df = orig_df.drop('daily_return', 1)

    join_df = pd.concat([orig_df, update_df])
    return join_df

def save_dataframe_to_csv(df, folder, ticker):
    df.to_csv(folder + ticker + '.csv')

def update_stocks(stock_folder):
    
    for x in tickers:
        try:
            print("Working on :", x)
            new_df = update_stock_data(x)
            save_dataframe_to_csv(new_df, stock_folder, x)
        except Exception as ex:
            print(ex)

update_stocks(folder)
    
#Defines tickers and folder
tickers = get_column_from_csv("/Users/jaechankwon/Downloads/stock_market/python_workspace/stock_list/Wilshire-20-Stocks.csv","Ticker")
# companys = ["TSLA", "INTC", "MSFT", "GOOGL", "ADBE", "QCOM", "AAPL", "AMZN", "LRCX", "KLAC", "NVDA", "SBUX", "MAR", "AMD", "NFLX", "LULU", "GOOG", "PYPL", "DASH"]    


for x in range(19):
        save_to_csv_from_yahoo(folder, tickers[x], 2024, 1, 1, end.year, end.month, end.day)
        
print("Finished")
# stocks_not_downloaded