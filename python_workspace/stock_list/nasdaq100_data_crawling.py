import datetime
import json
import logging
import os
import time
from concurrent.futures import ThreadPoolExecutor
from os import listdir
from os.path import isfile, join

import numpy as np
import pandas as pd
import yfinance as yf
from dateutil.relativedelta import relativedelta
from pandas_datareader import data as web

yf.pdr_override()

# 상수 선언
DATA_FOLDER = '/Users/jaechankwon/python_workspace/stock_list/'
UPDATE_FOLDER = '/Users/jaechankwon/python_workspace/stock_list/update/'
SLEEP_TIME = 10

# 로거 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def save_to_csv_from_yahoo(folder, ticker, start_date, end_date):
    try:
        logging.info(f"Getting data for {ticker}")
        df = web.get_data_yahoo(ticker, start_date, end_date)
        if df.empty:
            logging.warning(f"No data for {ticker}")
            return False
        time.sleep(SLEEP_TIME)
        df.to_csv(f'{folder}/{ticker}.csv')
        return True
    except Exception as ex:
        logging.error(f"Could not get data for {ticker}: {ex}")
        return False

def get_column_from_csv(file, col_name):
    try:
        df = pd.read_csv(file)
        if df.empty:
            logging.warning(f"No data in {file}")
            return None
    except FileNotFoundError:
        logging.error(f"File {file} does not exist")
        return None
    else:
        return df[col_name]

def get_df_from_csv(folder, ticker):
    file_path = f'{folder}/{ticker}.csv'
    try:
        df = pd.read_csv(file_path, index_col='Date', parse_dates=True)
        return df
    except FileNotFoundError:
        logging.error(f"File {file_path} doesn't exist")
        return None

def update_stock_data(ticker):
    try:
        update_df = get_df_from_csv(UPDATE_FOLDER, ticker)
        orig_df = get_df_from_csv(DATA_FOLDER, ticker)
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
    file_path = f'{folder}/{ticker}.csv'
    if df is not None:
        df.to_csv(file_path)
    else:
        logging.error(f"Unable to save dataframe for {ticker}")

def update_stocks():
    files = [x for x in listdir(DATA_FOLDER) if isfile(join(DATA_FOLDER, x))]
    tickers = [os.path.splitext(x)[0] for x in files]
    tickers.sort()

    for ticker in tickers:
        try:
            logging.info(f"Working on {ticker}")
            new_df = update_stock_data(ticker)
            save_dataframe_to_csv(new_df, DATA_FOLDER, ticker)
        except Exception as ex:
            logging.error(f"Error updating stocks: {ex}")

def generate_json_data(tickers):
    total_tickers = len(tickers)
    with ThreadPoolExecutor() as executor:
        for i, ticker in enumerate(tickers, start=1):
            executor.submit(process_ticker, ticker, i, total_tickers)

def process_ticker(ticker, idx, total):
    file_path = f'{DATA_FOLDER}/{ticker}.csv'
    try:
        df = pd.read_csv(file_path)
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

        with open(f'{DATA_FOLDER}/{ticker}.json', 'w') as json_file:
            json.dump(json_data, json_file, indent=2)
        logging.info(f"[{idx}/{total}] Generated JSON data for {ticker}")
    except FileNotFoundError:
        logging.error(f"{file_path} file not found")
    except Exception as ex:
        logging.error(f"Error generating JSON data for {ticker}: {ex}")

if __name__ == '__main__':
    end_date = datetime.datetime.now()
    start_date = end_date - relativedelta(months=1)

    tickers = get_column_from_csv("/Users/jaechankwon/python_workspace/stock_list/Wilshire-20-Stocks.csv", "Ticker")

if not tickers.empty:
    for ticker in tickers:
        save_to_csv_from_yahoo(DATA_FOLDER, ticker, start_date, end_date)
    logging.info("Finished downloading data")

    update_stocks()

    generate_json_data(tickers)
else:
    logging.error("Failed to get tickers from CSV file")