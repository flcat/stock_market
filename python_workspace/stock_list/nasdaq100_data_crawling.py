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
import json
import logging
from concurrent.futures import ThreadPoolExecutor

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

# 로거 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def save_to_csv_from_yahoo(folder, ticker, syear, smonth, sday, eyear, emonth, eday):
    start = datetime.datetime(syear, smonth, sday)
    end = datetime.datetime(eyear, emonth, eday)
    try:
        logging.info(f"Getting data for {ticker}")
        df = web.get_data_yahoo(ticker, start, end)
        if df.empty:
            logging.warning(f"No data for {ticker}")
            stocks_not_downloaded.append(ticker)
            return
        time.sleep(10)
        df.to_csv(f'{folder}/{ticker}.csv')
    except Exception as ex:
        stocks_not_downloaded.append(ticker)
        logging.error(f"Could not get data for {ticker}: {ex}")

#Returns a Named Column Data from CSV
def get_column_from_csv(file, col_name):
    try:
        df = pd.read_csv(file)
        if df.empty:
            logging.warning(f"No data for {ticker}")
            stocks_not_downloaded.append(ticker)
            return
    except FileNotFoundError:
        logging.error("File does not exist")
    else:
        return df[col_name]

# Returns a Named DataFrame from CSV
def get_df_from_csv(folder, ticker):
    try:
        df = pd.read_csv(f'{folder}/{ticker}.csv', index_col='Date', parse_dates=True)
    except FileNotFoundError:
        logging.error(f"File {ticker}.csv doesn't exist")
    else:
        return df

def update_stock_data(ticker):
    up_folder = '/Users/jaechankwon/Downloads/stock_market/python_workspace/stock_list/update/'
    stock_folder = folder
    try:
        update_df = get_df_from_csv(up_folder, ticker)
        orig_df = get_df_from_csv(stock_folder, ticker)
        if orig_df is None or update_df is None:
            return None
        orig_df.drop(orig_df.columns[orig_df.columns.str.contains('unnamed', case=False)],
                     axis=1, inplace=True)
        orig_df = orig_df.drop('daily_return', 1)
        join_df = pd.concat([orig_df, update_df])
        return join_df
    except Exception as ex:
        logging.error(f"Error updating stockDto data for {ticker}: {ex}")
        return None

def save_dataframe_to_csv(df, folder, ticker):
    if df is not None:
        df.to_csv(f'{folder}/{ticker}.csv')
    else:
        logging.error(f"Unable to save dataframe for {ticker}")

def update_stocks(stock_folder):
    for x in tickers:
        try:
            logging.info(f"Working on {x}")
            new_df = update_stock_data(x)
            save_dataframe_to_csv(new_df, stock_folder, x)
        except Exception as ex:
            logging.error(f"Error updating stocks: {ex}")

def generate_json_data(folder, tickers):
    total_tickers = len(tickers)
    with ThreadPoolExecutor() as executor:
        for i, ticker in enumerate(tickers, start=1):
            executor.submit(process_ticker, folder, ticker, i, total_tickers)

def process_ticker(folder, ticker, idx, total):
    try:
        df = pd.read_csv(f'{folder}/{ticker}.csv')
        json_data = []
        for _, row in df.iterrows():
            change = row['Close'] - row['Open']
            change_percent = (change / row['Open']) * 100
            json_obj = {
                'rt_cd': '0000',
                'msg_cd': '0000',
                'msg1': 'Success',
                'output': {
                    'rsym': ticker,
                    'zdiv': '',
                    'base': '',
                    'pvol': '',
                    'last': row['Close'],
                    'sign': '+' if change >= 0 else '-',
                    'diff': abs(change),
                    'rate': f"{change_percent:.2f}%",
                    'tvol': row['Volume'],
                    'tamt': '',
                    'ordy': ''
                }
            }
            json_data.append(json_obj)

        with open(f'{folder}/{ticker}.json', 'w') as json_file:
            json.dump(json_data, json_file, indent=2)
        logging.info(f"[{idx}/{total}] Generated JSON data for {ticker}")
    except FileNotFoundError:
        logging.error(f"{ticker}.csv file not found")
    except Exception as ex:
        logging.error(f"Error generating JSON data for {ticker}: {ex}")

update_stocks(folder)

#Defines tickers and folder
tickers = get_column_from_csv("/Users/jaechankwon/Downloads/stock_market/python_workspace/stock_list/Wilshire-20-Stocks.csv","Ticker")

# companys = ["TSLA", "INTC", "MSFT", "GOOGL", "ADBE", "QCOM", "AAPL", "AMZN", "LRCX", "KLAC", "NVDA", "SBUX", "MAR", "AMD", "NFLX", "LULU", "GOOG", "PYPL", "DASH"]
for x in range(19):
    save_to_csv_from_yahoo(folder, tickers[x], 2024, 1, 1, end.year, end.month, end.day)
logging.info("Finished downloading data")

# JSON 데이터 생성
generate_json_data(folder, tickers)